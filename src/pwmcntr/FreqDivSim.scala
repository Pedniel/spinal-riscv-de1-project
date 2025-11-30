import spinal.core.sim._
import spinal.core._
import spinal.lib._
import pwm._

object FreqDivSim extends App {
  SimConfig.compile(new PwmSystem).doSim {
    dut =>
    
    dut.clockDomain.forkStimulus(period = 5)
    dut.io.div_config #= 37 //37 -> 00000010 0101 --> 2,5
    dut.clockDomain.waitSampling(1)

    dut.io.pwm_cc_in #= 15
    dut.io.pwm_top_in #= 30
    dut.clockDomain.waitSampling(200)
//    pwm.io.clr_in #= false

    simSuccess()
    simThread.suspend()
  }
}
