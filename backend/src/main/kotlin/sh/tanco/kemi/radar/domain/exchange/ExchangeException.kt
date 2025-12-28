package sh.tanco.kemi.radar.domain.exchange

class DollarIndexNotFoundException(t: Throwable) : RuntimeException(t) {}

class ExchangeRateNotFoundException(t: Throwable) : RuntimeException(t) {}