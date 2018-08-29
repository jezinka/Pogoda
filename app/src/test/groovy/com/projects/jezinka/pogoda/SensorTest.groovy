package com.projects.jezinka.pogoda

import android.graphics.Color
import groovy.time.TimeCategory
import spock.lang.Specification
import spock.lang.Unroll
import spock.util.mop.Use

@Use(TimeCategory)
class SensorTest extends Specification {

    @Unroll
    def "GetBatteryColor for #vbat should return #expectedColor"() {

        given:
        Sensor sensor = new Sensor(vbat: vbat)

        when:
        int color = sensor.batteryColor

        then:
        noExceptionThrown()
        assert expectedColor == color

        where:
        vbat || expectedColor
        4.0  || Color.parseColor(Sensor.GREEN)
        3.5  || Color.parseColor(Sensor.GREEN)
        3.4  || Color.YELLOW
        3.2  || Color.RED
        3.1  || Color.RED
        1.2  || Color.RED

    }

    @Unroll
    def "IsSensorDead #testTimestamp"() {

        given:
        Sensor sensor = new Sensor(timestamp: testTimestamp.getTime())

        when:
        boolean isSensorDead = sensor.sensorDead

        then:
        noExceptionThrown()
        assert isSensorDead == isExpectedDead

        where:
        testTimestamp  || isExpectedDead
        new Date()     || false
        10.seconds.ago || false
        59.minutes.ago || false
        61.minutes.ago || true
        1.day.ago      || true
        10.years.ago   || true
    }
}
