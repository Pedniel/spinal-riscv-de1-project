package vexriscv.demo
import spinal.core._
import spinal.lib._

class Pwm() extends Component{
  val io = new Bundle {
    val top_in = in UInt(16 bits)
    val cc_in = in UInt(16 bits) 
    val clr_in = in Bool()
    val en_in = in Bool() 
    val pulse_o = out Bool() 
  }

  // Registers
  val ctr = Reg(UInt(16 bits)) init (0) 

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

