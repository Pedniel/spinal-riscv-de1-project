import spinal.core._
import spinal.lib._

class Pwm extends Component {

  val io = new Bundle {
    val top_in = in port UInt(16 bits) setName("top_in")
    val ctr_in = in port UInt(16 bits) setName("ctr_in")
    val clr_in = in port Bool() setName("clr_in")
    val pulse_o = out port Bool() setName("pulse_out")
  }

  // Registers
  val ctr = Reg(UInt(16 bits)) init (0) setName("ctr") 
  val on_off_switch = Reg(Bool()) init (True) setName("on_off_switch")

  // Functions
  def switch_bool(value: Bool): Bool = !value
  def TURN_PULSE_ON(): Bool = ((ctr === io.top_in) && on_off_switch)
  def TURN_PULSE_OFF(): Bool = ((ctr === io.ctr_in) && !on_off_switch)

  // For some reason, when assigning the switch to pulse directly,
  // I get type errors? Maybe a scala thing. TODO
  when (on_off_switch === True) {
    io.pulse_o := True
  } otherwise {
    io.pulse_o := False
  }
  
  // FSM(ish)
  when (io.clr_in === True) { 
    ctr := 0
    
  } elsewhen (TURN_PULSE_ON()) {
    ctr := 0
    on_off_switch := switch_bool(on_off_switch)
    
  } elsewhen (TURN_PULSE_OFF()) {
    ctr := 0
    on_off_switch := switch_bool(on_off_switch)
    
  } otherwise {
    ctr := ctr + 1    
  }  
}

object MyDesignVerilog extends App {
  SpinalVerilog(new Pwm)
} 
