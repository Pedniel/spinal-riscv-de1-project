package vexriscv.demo
import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba3.apb.{Apb3, Apb3Config, Apb3SlaveFactory}

class PwmSystem() extends Component{
  
  val io = new Bundle {
    val apb = slave(Apb3(Apb3Config(addressWidth=8, dataWidth=32)))
    val pwm_top_in  = in UInt(16 bits)  
    val pwm_cc_in   = in UInt(16 bits)
    val pwm_clr_in   = in Bool()
    val div_config  = in UInt(12 bits) 
    val pulse_o     = out Bool()
    val ctr_freqdiv_o = out UInt(8 bits)

    // Bus signals
    val limit = in UInt(width bits)

  }

  val busCtrl = Apb3SlaveFactory(io.apb)

  val divider  = new FreqDiv()

  val pwm = new Pwm()

  io.ctr_freqdiv_o := divider.io.ctr_o
    
  pwm.io.top_in     := io.pwm_top_in
  pwm.io.cc_in      := io.pwm_cc_in
  pwm.io.en_in      := divider.io.en_o
  pwm.io.clr_in     := io.pwm_clr_in
  
  divider.io.div_in := io.div_config
  
  io.pulse_o        := pwm.io.pulse_o

  def driveFrom(busCtrl: BusSlaveFactory, baseAddress: BigInt)(ticks: Seq[Bool], clears: Seq[Bool]) = new Area {
    // Offset 0 => clear/tick masks + bus
    val pwmEnable = busCtrl.createReadAndWrite(Bits(ticks.length bits), baseAddress + 0,0) init(0)
    val clrEnable = busCtrl.createReadAndWrite(Bits(clears.length bits), baseAddress + 0,16) init(0)
    val busClearing = False

    pwm.io.clr_in := (clrEnable & clears.asBits).orR | busClearing
    //io.tick := (pwmEnable  & ticks.asBits ).orR

    // Offset 4 => read/write limit (+ auto clear)
    busCtrl.driveAndRead(io.limit, baseAddress + 4)
    busClearing.setWhen(busCtrl.isWriting(baseAddress + 4))

    // Offset 8 => read timer value / write => clear timer value
    busCtrl.read(io.value, baseAddress + 8)
    busClearing.setWhen(busCtrl.isWriting(baseAddress + 8))
  }
}

}
  
object PwmSystem{  
  def main(args: Array[String]) {
    SpinalVerilog(new PwmSystem())
  }
}
