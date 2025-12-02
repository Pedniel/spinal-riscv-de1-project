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
    .doSim(new pwmcore.PwmSystem()) {
    dut =>

    // Block 1 
    dut.clockDomain.forkStimulus(period = 5)
    dut.io.div_config #= 37 //37 -> 0000 0010 0101 --> 2,5
    dut.io.pwm_clr_in #= true
    dut.clockDomain.waitSampling(1)
    dut.io.pwm_clr_in #= false
    dut.io.pwm_cc_in #= 5 
    dut.io.pwm_top_in #= 15
    dut.clockDomain.waitSampling(500)

    // Block 2
    dut.io.pwm_clr_in #= true
    dut.clockDomain.waitSampling(1)
    dut.io.div_config #= 0 // Test max value of 256, per datasheet page 1083
    dut.io.pwm_clr_in #= false
    dut.io.pwm_cc_in #= 1 
    dut.io.pwm_top_in #= 3    
    dut.clockDomain.waitSampling(1500)
    

    simSuccess()
    simThread.suspend()
  }
}
