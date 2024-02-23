## Vorbereitung
```bash 
az login
az account set --subscription "Azure for Students"
```

## ENV-Variablen
```bash
set LOCATION=switzerlandnorth
set GROUP_NAME=rg-blog-prod-001
set MY_SQL=blog-prod-001
set CONTAINERAPP_BACKEND="d-ca-blog-backend"

set BLOG_MYSQL_DB="blogdb"
set BLOG_MYSQL_USER="blog_user"
set BLOG_MYSQL_PW="db_password123"

Einrichten KeyCloak Setup

set GITHUB_USER="robinaeschlimann"
set GITHUB_TOKEN="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"

set RESOURCE_GROUP="rg-blog-prod-001"
set MYSQL_SERVER_NAME="blog-prod-001"
set KC_MYSQL_DB="kcdb"
set KC_MYSQL_USER="kc_user"
set KC_MYSQL_PW="db_password123"

set KEYCLOAK_ADMIN="admin"
set KEYCLOAK_ADMIN_PASSWORD="k[readme-images](readme-images)aIja_9h"

set LOG_ANALYTICS_WORKSPACE="d-log-blog"
set CONTAINERAPPS_ENVIRONMENT="d-ce-blog"
set CONTAINERAPP_KEYCLOAK="d-ca-blog-keycloak"

set STATIC_WEBAPP="wa-blog-001"
```

## Ressourcen-Gruppe erstellen
```bash
az group create --location %LOCATION% --resource-group %GROUP_NAME%
```

## MySQL Server erstellen
```bash
az mysql flexible-server create --name %MY_SQL% --resource-group %GROUP_NAME% --location %LOCATION% --public-access None --database-name test --admin-user db_admin --admin-password db_password123 --sku-name Standard_B1s --storage-size 32 --tier Burstable --version 8.0.21

az mysql flexible-server firewall-rule create --name %MY_SQL% --rule-name allowip --resource-group %GROUP_NAME% --start-ip-address 0.0.0.0

az mysql flexible-server parameter set --name sql_generate_invisible_primary_key --resource-group %GROUP_NAME% --server-name %MY_SQL% --value OFF
```
### MySQL Server verbinden
In der Azure CLI folgenden Befehl eingeben
```bash
az mysql flexible-server connect -n blog-prod-001 -u db_admin -p "db_password123" --interactive
```

Sobald die Verbindung steht, folgende Befehle ausführen:
```sql
CREATE DATABASE blogdb;
CREATE DATABASE kcdb;

CREATE USER 'blog_user'@'%' IDENTIFIED BY 'db_password123';
GRANT ALL PRIVILEGES ON blogdb.* TO 'blog_user'@'%';
FLUSH PRIVILEGES;

CREATE USER 'kc_user'@'%' IDENTIFIED BY 'db_password123';
GRANT ALL PRIVILEGES ON kcdb.* TO 'kc_user'@'%';
FLUSH PRIVILEGES;
```

## Keycloak aufsetzen
### Azure-CLI Tooling installieren
```bash
az extension add --name containerapp --upgrade
az provider register --namespace Microsoft.App
az provider register --namespace Microsoft.OperationalInsights
```

### Log Analytics Workspace erstellen
```bash
az monitor log-analytics workspace create ^
        --resource-group %RESOURCE_GROUP% ^
        --location %LOCATION% ^
        --workspace-name %LOG_ANALYTICS_WORKSPACE%
```
Danach die ClientId und Secret in ENV-Variablen speichern
```bash
For /f %i in ('az monitor log-analytics workspace show ^
            --resource-group %RESOURCE_GROUP% ^
            --workspace-name %LOG_ANALYTICS_WORKSPACE% ^
            --query customerId  ^
            --output tsv') do set "LOG_ANALYTICS_WORKSPACE_CLIENT_ID=%i"
For /f %i in ('az monitor log-analytics workspace get-shared-keys ^
            --resource-group %RESOURCE_GROUP% ^
            --workspace-name %LOG_ANALYTICS_WORKSPACE% ^
            --query primarySharedKey  ^
            --output tsv') do set "LOG_ANALYTICS_WORKSPACE_CLIENT_SECRET=%i"
```

### ContainerApp Environment erstellen
```bash
az containerapp env create ^
      --name %CONTAINERAPPS_ENVIRONMENT% ^
      --resource-group %RESOURCE_GROUP% ^
      --location %LOCATION% ^
      --logs-workspace-id %LOG_ANALYTICS_WORKSPACE_CLIENT_ID% ^
      --logs-workspace-key %LOG_ANALYTICS_WORKSPACE_CLIENT_SECRET%
```

### Container-App für KeyCloak erstellen
```bash
az containerapp create ^
    --name %CONTAINERAPP_KEYCLOAK% ^
    --resource-group %RESOURCE_GROUP% ^
    --environment %CONTAINERAPPS_ENVIRONMENT% ^
    --image ghcr.io/hftm-inf/edge-keycloak:0.5 ^
    --registry-server ghcr.io ^
    --registry-username %GITHUB_USER% ^
    --registry-password %GITHUB_TOKEN% ^
    --min-replicas 1 ^
    --max-replicas 1 ^
    --cpu 2.0 --memory 4.0Gi ^
    --ingress external --target-port 8080 ^
    --env-vars KC_DB=mysql KC_DB_URL=jdbc:mysql://%MYSQL_SERVER_NAME%.mysql.database.azure.com:3306/%KC_MYSQL_DB% ^
               KC_DB_USERNAME=%KC_MYSQL_USER% ^
               KC_DB_PASSWORD=%KC_MYSQL_PW% ^
               KEYCLOAK_ADMIN=%KEYCLOAK_ADMIN% ^
               KEYCLOAK_ADMIN_PASSWORD=%KEYCLOAK_ADMIN_PASSWORD%
```

### Hostname setzen
URL im Azure Portal abrufen und in die Umgebungsvariable setzen:
```bash
az containerapp update ^
        --name %CONTAINERAPP_KEYCLOAK% ^
        --resource-group %RESOURCE_GROUP% ^
        --min-replicas 0  ^
        --max-replicas 1  ^
        --cpu 1.5 --memory 3.0Gi ^
        --remove-env-vars KEYCLOAK_ADMIN KEYCLOAK_ADMIN_PASSWORD ^
        --set-env-vars KC_HOSTNAME_URL=https://%CONTAINERAPP_KEYCLOAK%.%CONTAINERAPPS_ENVIRONMENT%.azurecontainer.io/
```

### Keycloak konfigurieren und testen:

Secret: Sn5XMDAfqOt2KVFsGNL9vxQ0MVVdX9xk

```bash
http -v --form --auth backend-service:Sn5XMDAfqOt2KVFsGNL9vxQ0MVVdX9xk POST https://d-ca-blog-keycloak.livelybush-bf381778.switzerlandnorth.azurecontainerapps.io/realms/blog/protocol/openid-connect/token username=alice password=alice grant_type=password
```

## Backend als Container-App

```bash
az containerapp create ^
    --name %CONTAINERAPP_BACKEND% ^
    --resource-group %RESOURCE_GROUP% ^
    --environment %CONTAINERAPPS_ENVIRONMENT% ^
    --image ghcr.io/robinaeschlimann/blog-backend-ra:latest ^
    --registry-server ghcr.io ^
    --registry-username %GITHUB_USER% ^
    --registry-password %GITHUB_TOKEN% ^
    --cpu 1 --memory 2Gi ^
    --min-replicas 0 ^
    --ingress external --target-port 8080 ^
    --env-vars QUARKUS_OIDC_AUTH_SERVER_URL=https://d-ca-blog-keycloak.livelybush-bf381778.switzerlandnorth.azurecontainerapps.io/realms/blog ^
               QUARKUS_OIDC_CREDENTIALS_SECRET=n5XMDAfqOt2KVFsGNL9vxQ0MVVdX9xk ^
               QUARKUS_OIDC_CLIENT_ID=backend-service ^
               QUARKUS_DATASOURCE_JDBC_URL=jdbc:mysql://%MYSQL_SERVER_NAME%.mysql.database.azure.com:3306/%BLOG_MYSQL_DB% ^
               QUARKUS_DATASOURCE_USERNAME=%BLOG_MYSQL_USER% ^
               QUARKUS_DATASOURCE_PASSWORD=%BLOG_MYSQL_PW%
```

## Frontend als Staic-Web-App

```bash

az staticwebapp create ^
    --name %STATIC_WEBAPP% ^
    --resource-group %RESOURCE_GROUP% ^
    --source https://github.com/robinaeschlimann/angular-blog-app-robin-aeschlimann ^
    --location "westeurope" ^
    --branch main ^
    --output-location "dist/blog-ra" ^
    --sku free ^
    --login-with-github
```

### Ausgabe URL
```bash
az staticwebapp show ^
    --name %STATIC_WEBAPP% ^
    --query "defaultHostname"
```





