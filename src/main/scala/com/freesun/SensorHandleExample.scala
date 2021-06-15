package com.freesun

import comm.models.{Factor, Station, StationData, Structure}
import org.jeasy.rules.api.{Facts, Rules, RulesEngine}
import org.jeasy.rules.core.DefaultRulesEngine
import org.jeasy.rules.mvel.MVELRuleFactory
import org.jeasy.rules.support.reader.YamlRuleDefinitionReader
import org.joda.time.DateTime

import scala.io.Source

/**
  * Created by yww08 on 2021/6/15.
  */
object SensorHandleExample {
    var rules: Rules = _
    var engine: RulesEngine = _

    def main(args: Array[String]): Unit = {
        val ruleFactory = new MVELRuleFactory(new YamlRuleDefinitionReader)
        val source = Source.fromInputStream(getClass.getResourceAsStream("/sensors.yml"))
        val weatherRule = ruleFactory.createRule(source.bufferedReader())
        source.close()

        rules = new Rules()
        rules.register(weatherRule)

        engine = new DefaultRulesEngine()

        val s = Station("s1", 1, Structure("", 1, "struct"), Array(), Factor(1, "fac", "1001", "wenshidu", Array()), false)
        val sd = StationData(s, DateTime.now, "task", Some(Map("temperature" -> 33.1)))
        handle(sd)
    }

    def handle(data: StationData): Unit = {
        val facts = new Facts()
        facts.put("sensor", BoxData(data))

        engine.fire(rules, facts)
    }
}

// like a POJO
case class BoxData(var data: StationData) {
    def validate(): Unit = {
        data = Validator.handle(data)
    }

    def analyze(): Unit = {
        data = Analyzer.handle(data)
    }

    def storage(): Unit = {
        Storage.handle(data)
    }
}

object Validator {
    def handle(data: StationData): StationData = {
        println("handled validator")
        data
    }
}

object Analyzer {
    def handle(data: StationData): StationData = {
        println("handled analyze")
        data
    }
}

object Storage {
    def handle(data: StationData): Unit = {
        println(s"data storaged: ${data.data.get}")
    }
}
