package com.lucassilva.api_rest.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private List<String> erros;

    public ErrorResponse(LocalDateTime timestamp,
                         int status,
                         List<String> erros){
        this.timestamp = timestamp;
        this.status = status;
        this.erros = erros;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public List<String> getErros() {
        return erros;
    }
}
