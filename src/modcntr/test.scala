import spinal.core.sim._

object ModcntrSim extends App {
  SimConfig.withWave.compile(new Modcntr).doSim {
    dut =>
    
    dut.clockDomain.forkStimulus(period = 1) // TODO half or full period? --> Full I believe
    
    dut.io.top_in #= 15
    
    dut.clockDomain.waitSampling(20)

    dut.io.top_in #= 20

    dut.clockDomain.waitSampling(25)

    simSuccess()
    simThread.suspend()
    
    // assert(dut.io.pulse.toBoolean == )
    
  }
}

//object MyDesignVerilog extends App {
//  SpinalVerilog(new Modcntr)
//  SpinalVhdl(new Modcntr)
//} 
