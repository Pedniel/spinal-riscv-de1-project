package pwmcore
import spinal.core._
import spinal.lib._
  
class PwmSystem() extends Component{
  
  val io = new Bundle {
    val pwm_top_in  = in UInt(16 bits)  
    val pwm_cc_in   = in UInt(16 bits)
    val div_config  = in UInt(12 bits) 
    val pulse_o     = out Bool()
  }

  val divider  = new FreqDiv()
  val pwm = new Pwm() 
    
  pwm.io.top_in := io.pwm_top_in

  pwm.io.cc_in      := io.pwm_cc_in
  divider.io.div_in := io.div_config
  pwm.io.en_in      := True
  io.pulse_o        := pwm.io.pulse_o

  when (divider.io.en_o){ 
    pwm.io.en_in := True  
  } otherwise {
    pwm.io.en_in := False 
  }                       

  pwm.io.clr_in := False
}
  
object PwmSystem{  
  def main(args: Array[String]) {
    SpinalVhdl(new PwmSystem())
  }
}
