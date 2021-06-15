package com.freesun

import org.jeasy.rules.api.{Facts, Rules}
import org.jeasy.rules.core.DefaultRulesEngine
import org.jeasy.rules.mvel.MVELRuleFactory
import org.jeasy.rules.support.reader.YamlRuleDefinitionReader

import scala.io.Source

object main {
    def main(args: Array[String]): Unit = {
        val ruleFactory = new MVELRuleFactory(new YamlRuleDefinitionReader)
        val source = Source.fromInputStream(getClass.getResourceAsStream("/weather-rule.yml"))
        val weatherRule = ruleFactory.createRule(source.bufferedReader())
        source.close()

        val facts = new Facts()
        facts.put("rain", true)

        val rules = new Rules()
        rules.register(weatherRule)

        val rulesEngine = new DefaultRulesEngine()
        rulesEngine.fire(rules, facts)
    }
}
