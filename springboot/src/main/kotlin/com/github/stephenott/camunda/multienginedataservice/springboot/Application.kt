package com.github.stephenott.camunda.multienginedataservice.springboot

import com.github.stephenott.camunda.multienginedataservice.MultiEngineDataServiceProcessEnginePlugin
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component


@SpringBootApplication
@EnableProcessApplication
class Application

fun main(args: Array<String>) = runApplication<Application>(*args).let { }

@Component
class MyMongoHistoryMongoHistoryEventHandlerProcessEnginePlugin: MultiEngineDataServiceProcessEnginePlugin()
