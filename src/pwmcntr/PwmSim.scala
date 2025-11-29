import spinal.core.sim._
import spinal.core._
import spinal.lib._

object PwmSim extends App {
  SimConfig.withWave.compile(new Pwm).doSim {
    dut =>
    
    dut.clockDomain.forkStimulus(period = 5) // TODO half or full period? --> Full I believe
    
    dut.io.top_in #= 15
    dut.io.ctr_in #= 30
    dut.io.clr_in #= false
    dut.clockDomain.waitSampling(100)
    
    dut.io.clr_in #= true
    dut.clockDomain.waitSampling(1)
    
    dut.io.clr_in #= false
    dut.io.top_in #= 20
    dut.io.ctr_in #= 5    
    dut.clockDomain.waitSampling(250)

    simSuccess()
    simThread.suspend()
    
    // assert(dut.io.pulse.toBoolean == )
    
  }
}

//object MyDesignVerilog extends App {
//  SpinalVerilog(new Modcntr)
//  SpinalVhdl(new Modcntr)
//} 
