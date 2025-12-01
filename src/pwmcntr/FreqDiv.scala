package pwmcore
import spinal.core._
import spinal.lib._

class FreqDiv() extends Component {
  
  val io = new Bundle {
    val div_in = in UInt(12 bits) 
    val en_o   = out Bool()       
  }

  val frac_acc = Reg(UInt(5 bits)) init(0) 
//  val div_int  = Reg(UInt(8.bits)) init(0)
  val ctr      = Reg(UInt(8 bits)) init(0) 
//io.div_in(11 downto 4)
  // FSM
  when (frac_acc.msb === True) {
    frac_acc := (False ## frac_acc(3 downto 0)).asUInt
    io.en_o := False
  } elsewhen (ctr === (io.div_in >> 4)) {
    ctr := 0
    io.en_o := True
    frac_acc := frac_acc(3 downto 0) +^ io.div_in(3 downto 0)
  } otherwise {
    io.en_o := False
    ctr := ctr + 1
  }
 
}

