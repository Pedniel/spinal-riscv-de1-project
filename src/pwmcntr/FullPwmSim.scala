import spinal.core.sim._
import spinal.core._
import spinal.lib._
import pwmcore._

object FullPwmSim extends App {
  SimConfig
    .withWave 
    .withConfig(
      SpinalConfig(defaultClockDomainFrequency = FixedFrequency(100 MHz))
    )
    .compile { 
      val dut = new pwmcore.PwmSystem()
      dut.divider.io.en_o.simPublic() // make signal visible for our testing
      dut
    }
    .doSim { dut =>

      def runFreqDivVerify(cycles: Int) = {
      var pulses = 0
        for(i <- 0 until cycles) {
          if(dut.divider.io.en_o.toBoolean) pulses += 1
          dut.clockDomain.waitSampling()
        }
        val ratio = if(pulses > 0) cycles.toDouble / pulses else 0.0
        val divIn = dut.io.div_config.toInt
      // 8 4 fixed point to get expected results to compare  vs actual
      val expected = if(divIn == 0) 256.0 else (divIn >> 4) + (divIn & 0xF)/16.0
      
        println(f"DivConf: $divIn%3d | Exp: $expected%6.4f | Sim: $ratio%6.4f | Pulses: $pulses")
      }

    dut.clockDomain.forkStimulus(period = 5)

      // PWM Verification
      def runPwmVerify(cycles: Int) = {
        var highCount = 0
        var lowCount = 0
        var lastState = false
        var periods = 0

        for(i <- 0 until cycles) {
          val current = dut.io.pulse_o.toBoolean
          if(current) highCount += 1 else lowCount += 1

          if(current && !lastState) periods += 1
          lastState = current

          dut.clockDomain.waitSampling()
        }

        val dutyCycle = if(highCount + lowCount > 0)
          (highCount.toDouble / (highCount + lowCount)) * 100 else 0.0
        val top = dut.io.pwm_top_in.toInt
        val cc = dut.io.pwm_cc_in.toInt
        val expectedDuty = if(top > 0) (cc.toDouble / top) * 100 else 0.0

        println(f"TOP: $top%3d | CC: $cc%3d | Expected: $expectedDuty%5.1f%% | Actual: $dutyCycle%5.1f%% | Periods: $periods")
      }

      dut.clockDomain.forkStimulus(period = 5)

      println("\n=== FreqDiv Tests ===")
      // FreqDiv Test 1
      dut.io.div_config #= 37
      dut.io.pwm_clr_in #= true
      dut.clockDomain.waitSampling(1)
      dut.io.pwm_clr_in #= false
      dut.io.pwm_cc_in #= 5
      dut.io.pwm_top_in #= 15
      runFreqDivVerify(2000)

      // FreqDiv Test 2
      dut.io.div_config #= 34
      dut.io.pwm_clr_in #= true
      dut.clockDomain.waitSampling(1)
      dut.io.pwm_clr_in #= false
      runFreqDivVerify(2000)

      // FreqDiv Test 3
      dut.io.div_config #= 35
      dut.io.pwm_clr_in #= true
      dut.clockDomain.waitSampling(1)
      dut.io.pwm_clr_in #= false
      runFreqDivVerify(2000)

      // FreqDiv Test 4
      dut.io.div_config #= 0
      dut.io.pwm_clr_in #= true
      dut.clockDomain.waitSampling(1)
      dut.io.pwm_clr_in #= false
      dut.io.pwm_cc_in #= 1
      dut.io.pwm_top_in #= 3
      runFreqDivVerify(100000)

      println("\n=== PWM Tests ===")
      // PWM Test 1: 50% duty
      dut.io.div_config #= 16 // div = 1.0
      dut.io.pwm_clr_in #= true
      dut.clockDomain.waitSampling(1)
      dut.io.pwm_clr_in #= false
      dut.io.pwm_cc_in #= 5
      dut.io.pwm_top_in #= 10
      runPwmVerify(4000)

      // PWM Test 2: 25% duty
      dut.io.pwm_clr_in #= true
      dut.clockDomain.waitSampling(1)
      dut.io.pwm_clr_in #= false
      dut.io.pwm_cc_in #= 2
      dut.io.pwm_top_in #= 8
      runPwmVerify(4000)

      // PWM Test 3: 75% duty
      dut.io.pwm_clr_in #= true
      dut.clockDomain.waitSampling(1)
      dut.io.pwm_clr_in #= false
      dut.io.pwm_cc_in #= 15
      dut.io.pwm_top_in #= 20
      runPwmVerify(10000)

      simSuccess()
      simThread.suspend()
  }
}
