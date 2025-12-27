package vexriscv.demo
import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba3.apb.{Apb3, Apb3Config, Apb3SlaveFactory}
import spinal.lib.bus.misc.BusSlaveFactory

class PwmSystem() extends Component{
  
  val io = new Bundle {
    val apb = slave(Apb3(Apb3Config(addressWidth=8, dataWidth=32)))
    val pulse_o = out Bool()
    val ctr_freqdiv_o = out UInt(8 bits)
  }

  val divider  = new FreqDiv()
  val pwm = new Pwm()

  io.ctr_freqdiv_o := divider.io.ctr_o
  io.pulse_o        := pwm.io.pulse_o

  val busCtrl = Apb3SlaveFactory(io.apb)

  driveFromBus(busCtrl, 0x00)

  def driveFromBus(busCtrl: BusSlaveFactory, baseAddress: BigInt) = new Area {
    val pwmTop = busCtrl.createReadAndWrite(UInt(16 bits), baseAddress + 0x00) init(0)
    pwm.io.top_in := pwmTop

    val pwmCompare = busCtrl.createReadAndWrite(UInt(16 bits), baseAddress + 0x04) init(0)
    pwm.io.cc_in := pwmCompare

    val divConfig = busCtrl.createReadAndWrite(UInt(12 bits), baseAddress + 0x08) init(0)
    divider.io.div_in := divConfig

    val control = busCtrl.createReadAndWrite(Bits(32 bits), baseAddress + 0x0C) init(0)
    val pwmEnable = control(0)
    val pwmClear = control(1)

    pwm.io.en_in := pwmEnable & divider.io.en_o
    pwm.io.clr_in := pwmClear

  }
}

object PwmSystem{  
  def main(args: Array[String]) {
    SpinalVerilog(new PwmSystem())
  }
}
