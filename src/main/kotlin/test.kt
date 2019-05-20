import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.client.reactive.ReactorResourceFactory
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient

fun main() {

    val mapper = ObjectMapper()
    val webClient = webClient(mapper)

    webClient
        .baseUrl("https://google.com")
        .build()
        .get()
        .exchange()
        .log()
        .block()
}

fun webClient(mapper: ObjectMapper): WebClient.Builder {
    val strategies = ExchangeStrategies
        .builder()
        .codecs { c ->
            c.defaultCodecs().jackson2JsonDecoder(Jackson2JsonDecoder(mapper, APPLICATION_JSON))
        }.build()

    val resourceFactory = resourceFactory()
    resourceFactory.afterPropertiesSet()

    val connector = ReactorClientHttpConnector(resourceFactory) { client -> client }

    return WebClient
        .builder()
        .exchangeStrategies(strategies)
        .clientConnector(connector)
}

fun resourceFactory(): ReactorResourceFactory {
    val factory = ReactorResourceFactory()
    factory.isUseGlobalResources = false
    return factory
}
