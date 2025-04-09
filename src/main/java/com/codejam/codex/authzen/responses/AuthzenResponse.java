package com.codejam.codex.authzen.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AuthzenResponse<T>
{
    private static final String SUCCESSFUL = "successful";
    private static final String UNSUCCESSFUL = "unsuccessful";

    @Setter
    private String status;

    @Setter
    private List<T> results;

    /**
     * Default constructor, initializes with a "successful" status and an empty results list.
     */
    public AuthzenResponse()
    {
        status = SUCCESSFUL;
        results = new ArrayList<>();
    }

    /**
     * Constructor that initializes with a provided data object.
     *
     * @param data The data object to include in the results.
     */
    public AuthzenResponse( T data )
    {
        this();
        addResult( data );
    }


    /**
     * Adds a data object to the results list if it is not null.
     *
     * @param data The data object to add to the results.
     */
    private void addResult(T data) {
        if (data != null) {
            results.add(data);
        }
    }
}
