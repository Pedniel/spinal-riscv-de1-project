import spinal.core.sim._
import spinal.core._
import spinal.lib._
import pwmcore._

object FreqDivSim extends App {
  SimConfig
    .withWave 
    .withConfig(
      SpinalConfig(defaultClockDomainFrequency = FixedFrequency(100 MHz))
    )
    .doSim(new pwmcore.PwmSystem()) {
    dut =>
    
    dut.clockDomain.forkStimulus(period = 5)
    dut.io.div_config #= 37 //37 -> 00000010 0101 --> 2,5
    dut.clockDomain.waitSampling(1)

    dut.io.pwm_cc_in #= 5 
    dut.io.pwm_top_in #= 15
    dut.clockDomain.waitSampling(500)
//    pwm.io.clr_in #= false

    simSuccess()
    simThread.suspend()
  }
}
