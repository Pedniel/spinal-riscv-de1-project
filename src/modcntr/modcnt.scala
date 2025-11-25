import spinal.core._
import spinal.lib._

class Modcntr extends Component {

  val io = new Bundle {
    val top_in = in port UInt(16 bits) setName("top_in")
    val clr_in = in port Bool() setName("clr_in")
    val pulse = out port Bool() setName("pulse_out")
  }
  
  val ctr = Reg(UInt(16 bits)) init (0) setName("ctr") 
  
  io.pulse := False  

  when (io.clr_in === True) { 
    ctr := 0
  } elsewhen (ctr === io.top_in) {
    ctr := 0
    io.pulse := True
  } otherwise {
    ctr := ctr + 1
  }
  
}

object MyDesignVerilog extends App {
  SpinalVerilog(new Modcntr)
} 
