package sh.tanco.kemi.radar.application.config

import org.koin.dsl.module
import sh.tanco.kemi.radar.application.service.IndicatorService

val appModule = module {
    /*
     * Application
     */
    single<IndicatorService> { IndicatorService(get(), get(), get(), get()) }
}