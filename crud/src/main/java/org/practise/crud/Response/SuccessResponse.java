package org.practise.crud.Response;

import lombok.Data;

@Data
public class SuccessResponse<T> implements Response {
    private ResponseStatus status = ResponseStatus.success;

    private T data;

    public SuccessResponse(T data){
        this.data = data;
    }
}
