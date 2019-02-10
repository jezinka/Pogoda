package com.projects.jezinka.pogoda

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import spock.lang.Specification

import java.lang.reflect.Type

class MoshiSensorTest extends Specification {

    def "test adapter list"() {
        given:
        String json = '[{"label":"Balkon","id":3420594,"hum_temp":5.72,"stamp":1549786147000,"hum_hum":5.54,"lux":2283.48,"bar_pres_rel":1010.06,"vbat":3.75,"vreg":2.84},{"label":"Salon","id":3420593,"hum_temp":23.81,"stamp":1549786144000,"hum_hum":48,"lux":51.5495,"bar_pres_rel":null,"vbat":3.64,"vreg":2.76},{"label":"Biuro","id":3420595,"hum_temp":23.28,"stamp":1549786148000,"hum_hum":51.51,"lux":179.711,"bar_pres_rel":null,"vbat":3.69,"vreg":2.83},{"label":"Łazienka","id":3420592,"hum_temp":26.4,"stamp":1549786133000,"hum_hum":50,"lux":0.1294,"bar_pres_rel":null,"vbat":3.67,"vreg":2.79}]'
        Moshi moshi = new Moshi.Builder().build()
        Type type = Types.newParameterizedType(List.class, Sensor.class)
        JsonAdapter<List<Sensor>> adapter = moshi.adapter(type)

        when:
        List<Sensor> sensors = adapter.fromJson(json)

        then:
        noExceptionThrown()
        assert sensors.size() == 4
        assert sensors.label.sort() == (['Balkon', 'Łazienka', 'Salon', 'Biuro'].sort())
    }

    def "test adapter one sensor"() {
        given:
        String json = '[{"label":"Balkon","id":3420594,"hum_temp":5.72,"stamp":1549786147000,"hum_hum":5.54,"lux":2283.48,"bar_pres_rel":1010.06,"vbat":3.75,"vreg":2.84}]'
        Moshi moshi = new Moshi.Builder().build()
        Type type = Types.newParameterizedType(List.class, Sensor.class)
        JsonAdapter<List<Sensor>> adapter = moshi.adapter(type)

        when:
        Sensor sensor = adapter.fromJson(json)[0]
        println sensor

        then:
        noExceptionThrown()
        assert sensor.label == 'Balkon'
        assert sensor.id == 3420594
        assert sensor.vbat == 3.75
        assert sensor.vreg == 2.84
    }
}