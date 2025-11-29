import spinal.core.sim._
import spinal.core._
import spinal.lib._

object PwmSim extends App {
  SimConfig.withWave.compile(new Pwm).doSim {
    dut =>
    
    dut.clockDomain.forkStimulus(period = 5) // TODO half or full period? --> Full I believe
                                
    dut.io.en_in #= true // Always enable for now
    
    dut.io.top_in #= 40
    dut.io.cc_in #= 30
    dut.io.clr_in #= false
    dut.clockDomain.waitSampling(100)
    
    dut.io.clr_in #= true
    dut.clockDomain.waitSampling(1)
    
    dut.io.clr_in #= false
    dut.io.top_in #= 20
    dut.io.cc_in #= 5    
    dut.clockDomain.waitSampling(250)

    dut.io.clr_in #= true
    dut.clockDomain.waitSampling(1)
    
    dut.io.clr_in #= false
    dut.io.top_in #= 30
    dut.io.cc_in #= 40    
    dut.clockDomain.waitSampling(200)

    dut.io.clr_in #= true
    dut.clockDomain.waitSampling(1)
    
    dut.io.clr_in #= false
    dut.io.top_in #= 15
    dut.io.cc_in #= 15    
    dut.clockDomain.waitSampling(200)

    dut.io.clr_in #= true
    dut.clockDomain.waitSampling(1)
    
    dut.io.clr_in #= false
    dut.io.top_in #= 30
    dut.io.cc_in #= 15    
    dut.clockDomain.waitSampling(200)

    dut.io.en_in #= false

    dut.clockDomain.waitSampling(50)
    dut.io.en_in #= true
    dut.clockDomain.waitSampling(10)
    dut.io.en_in #= false
    dut.clockDomain.waitSampling(10)
    dut.io.en_in #= true
    dut.clockDomain.waitSampling(10)
    dut.io.en_in #= false
    dut.clockDomain.waitSampling(10)
    dut.io.en_in #= true
    dut.clockDomain.waitSampling(10)

    
    simSuccess()
    simThread.suspend()
    
    // assert(dut.io.pulse.toBoolean == )
    
  }
}

//object MyDesignVerilog extends App {
//  SpinalVerilog(new Modcntr)
//  SpinalVhdl(new Modcntr)
//} 
