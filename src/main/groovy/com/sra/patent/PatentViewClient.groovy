package com.sra.patent


import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException

import javax.validation.constraints.NotBlank

import static io.micronaut.http.HttpHeaders.ACCEPT
import static io.micronaut.http.HttpHeaders.CONTENT_TYPE
import static io.micronaut.http.MediaType.APPLICATION_JSON

@SuppressWarnings('SpellCheckingInspection')
@Header(name = ACCEPT, value = APPLICATION_JSON)
@Header(name = CONTENT_TYPE, value = APPLICATION_JSON)
@Header(name = "X-Api-Key", value="YObeUkSB.2e4XutVfLPaa1E8T0zGuwdq2M26OED0T")
@Client('https://search.patentsview.org/api/v1/')
interface PatentViewClient {

    /**
     * @param body A Lucene-syntax query request
     * @return the published data regarding the patent (or null)
     * @see <a href="https://search.patentsview.org/docs/docs/Search%20API/SearchAPIReference#request-parameters">
     * search.patentsview.org</a> for request parameters
     */
    @Header(name = ACCEPT, value = APPLICATION_JSON)
    @Header(name = CONTENT_TYPE, value = APPLICATION_JSON)
    @Header(name = "X-Api-Key", value="YObeUkSB.2e4XutVfLPaa1E8T0zGuwdq2M26OED0T")
    @Post('/patent/')
    def search(@NotBlank @Body String body) throws HttpClientResponseException

}