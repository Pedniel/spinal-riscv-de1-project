package pwm
import spinal.core._
import spinal.lib._
import spinal.core.sim._


case class PwmSystem() extends Component {


  val io = new Bundle {
    val pwm_top_in  = in UInt(16 bits) setName("top_level_top_in") 
    val pwm_cc_in   = in UInt(16 bits) setName("top_level_cc_in")  
    val div_config  = in UInt(12 bits) setName("top_level_div_in") 
    
    val pulse_o     = out Bool()
  }

  noIoPrefix()
  val divider  = new FreqDiv()
  val pwm_core = new Pwm()
  
  pwm_core.io.top_in := U"16'x0010"
  pwm_core.io.cc_in  := U"16'x0005"
  
  divider.io.div_in  := U"12'x025" // 2,5

  when (divider.io.en_o){
    pwm_core.io.en_in := True
    
  } otherwise {
    pwm_core.io.en_in := False
    
  }

  when (pwm_core.io.pulse_o){    
    io.pulse_o := True    
  } otherwise {    
    io.pulse_o := True
  } 

  pwm_core.io.clr_in := False 
}

object MyTopVerilog extends App {
  SpinalVerilog(new PwmSystem)
} 
