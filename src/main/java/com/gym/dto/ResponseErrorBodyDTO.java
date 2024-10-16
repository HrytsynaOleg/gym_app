package com.gym.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseErrorBodyDTO {
   private List<String> errors;
}
