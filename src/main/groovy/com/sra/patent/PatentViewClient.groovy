package com.sra.patent


import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Produces
import io.micronaut.http.client.annotation.Client

import javax.validation.constraints.NotBlank

import static io.micronaut.http.MediaType.APPLICATION_JSON

@Client('https://search.patentsview.org/api/v1/')
interface PatentViewClient {

    /**
     * @param body A Lucene-syntax query request
     * @return the published data regarding the patent (or null)
     * @see <a href="https://search.patentsview.org/docs/docs/Search%20API/SearchAPIReference#request-parameters">
     * search.patentsview.org</a> for request parameters
     */
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @Post('/patent/')
    def search(@NotBlank String body)

}