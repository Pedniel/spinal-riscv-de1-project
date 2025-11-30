package pwm
import spinal.core._
import spinal.lib._
import spinal.core.sim._

class Pwm() extends Component {

  val io = new Bundle {
    val top_in = in UInt(16 bits) setName("top_in")  // Defines the whole PWM period
    val cc_in = in UInt(16 bits) setName("cc_in") 
    val clr_in = in Bool() setName("clr_in") 
    val en_in = in Bool() setName("en_in")
    val pulse_o = out Bool() setName("pulse_out")
  }

  // Registers
  val ctr = Reg(UInt(16 bits)) init (0) setName("ctr") 

  // Functions
  def PWM_CYCLE_LENGTH_REACHED(): Bool = (ctr === io.top_in)

  // Pulse combinational logic
  when (io.cc_in > io.top_in) { // RP2350 Sheet Page 1080 " A CC value of TOP + 1 (..) produces a 100% output"
    io.pulse_o := True
  } elsewhen (ctr >= io.cc_in) {
    io.pulse_o := False    
  } otherwise {
    io.pulse_o := True
  }
  
  // FSM(ish)
  when (io.clr_in === True) { 
    ctr := 0
    
  } elsewhen(io.en_in === True) {
      when (PWM_CYCLE_LENGTH_REACHED()) {
        ctr := 0
      } otherwise {
        ctr := ctr + 1    
      }
  }

}

object MyDesignVerilog extends App {
  SpinalVerilog(new Pwm)
} 
