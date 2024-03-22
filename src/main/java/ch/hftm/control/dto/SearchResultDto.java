package ch.hftm.control.dto;

import java.util.List;

public record SearchResultDto<T> (List<T> result, long resultCount) {
}
