package pwmcore
import spinal.core._
import spinal.lib._
  
class PwmSystem() extends Component{
  
  val io = new Bundle {
    val pwm_top_in  = in UInt(16 bits)  
    val pwm_cc_in   = in UInt(16 bits)
    val pwm_clr_in   = in Bool()
    val div_config  = in UInt(12 bits) 
    val pulse_o     = out Bool()
    val ctr_freqdiv_o = out UInt(8 bits)
  }

  val divider  = new FreqDiv()
  val pwm = new Pwm()

  io.ctr_freqdiv_o := divider.io.ctr_o
    
  pwm.io.top_in     := io.pwm_top_in
  pwm.io.cc_in      := io.pwm_cc_in
  pwm.io.en_in      := divider.io.en_o
  pwm.io.clr_in     := io.pwm_clr_in
  
  divider.io.div_in := io.div_config
  
  io.pulse_o        := pwm.io.pulse_o

}
  
object PwmSystem{  
  def main(args: Array[String]) {
    SpinalVerilog(new PwmSystem())
  }
}
