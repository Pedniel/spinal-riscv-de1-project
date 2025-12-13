package pwmcore
import spinal.core._
import spinal.lib._

class FreqDiv() extends Component {
  val io = new Bundle {
    val div_in = in UInt(12 bits) 
    val en_o   = out Bool()
    val ctr_o = out UInt(8 bits)
  }

  // Registers
  val frac_acc = Reg(UInt(5 bits)) init(0) 
  val ctr      = Reg(UInt(8 bits)) init(0)

  // comb logic
  val frac_acc_comb = UInt(5 bits)
  
  io.ctr_o := ctr
  frac_acc_comb := frac_acc(3 downto 0) +^ io.div_in(3 downto 0)
  // FSM
  when (frac_acc.msb === True) {
    frac_acc := (False ## frac_acc_comb(3 downto 0)).asUInt // Reset to frac_acc + div_in, not just frac_acc
    io.en_o := False
  } elsewhen (ctr === ((io.div_in >> 4) - 1)) { // '- 1 ' eg. 2(decimal) equals ctr 0 , 1: 2 cycles
    ctr := 0
    io.en_o := True
    frac_acc := frac_acc(3 downto 0) +^ io.div_in(3 downto 0)
  } otherwise {
    io.en_o := False
    ctr := ctr + 1
  }
 
}

