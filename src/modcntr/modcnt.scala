import spinal.core._
import spinal.lib._

class Modcntr extends Component {

  val io = new Bundle {
    val top_in = in port UInt(16 bits)
    val pulse = out port Bool()
  }
  
  val top = Reg(UInt(16 bits)) init (0) setName("top")
  val ctr = Reg(UInt(16 bits)) init (0) setName("ctr")
  
  top := io.top_in
  io.pulse := False  

  when (top =/= io.top_in) {
    ctr := 0
  } elsewhen (ctr === io.top_in) {
    ctr := 0
    io.pulse := True
  } otherwise {
    ctr := ctr + 1
  }
  
}
