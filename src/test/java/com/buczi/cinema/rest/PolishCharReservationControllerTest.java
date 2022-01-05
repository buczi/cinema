package com.buczi.cinema.rest;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DISCLAIMER !!!
 * Tests should be only run when the application is running.
 * Tests check validation of incoming request bodies
 */
@Tag("pl-api-test")
class PolishCharReservationControllerTest {

    private static URI RESERVE_URI;
    private static final  HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final String[] HEADERS = new String[]{"Accept", "application/json","Content-Type","application/json"};
    static
    {
        try
        {
            RESERVE_URI = new URI("http://localhost:8080/reserve");
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    void givenCorrectRequest_ReturnOKStatus() throws Exception
    {
        // given
        final var request = HttpRequest.newBuilder()
                .uri(RESERVE_URI)
                .headers(HEADERS)
                .POST(HttpRequest.BodyPublishers.ofString(createBody("Dariąwęź","Woźniakąćęłńóśźż")))
                .build();

        // when
        final var response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void givenCorrectRequestWithDashedSurname_ReturnOKStatus() throws Exception
    {
        // given
        final var request = HttpRequest.newBuilder()
                .uri(RESERVE_URI)
                .headers(HEADERS)
                .POST(HttpRequest.BodyPublishers.ofString(createBody("Maria","Żukowska-Ączek")))
                .build();

        // when
        final var response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private String createBody(final String name, final String surname)
    {
        return "{\"name\": \"" + name + "\", \"surname\": \"" + surname + "\", \"submitDate\": \"0\", \"eventId\": \"3\" ,\"reservedSeats\": [{ \"first\": \"1\", \"second\":\"ADULT\"},{\"first\": \"2\", \"second\":\"CHILD\"}]}";
    }
}
