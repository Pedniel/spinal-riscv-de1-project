`define CLK_CYCLE 10
`define CLK_HALF_CYCLE 5


module tb_top;
   
   reg clk;
   reg reset_n;
   reg [15:0] top;
   reg clr;
   wire pulse_o;
       
   initial begin
      clk = 0;
      clr = 0;
      forever #`CLK_HALF_CYCLE clk = ~clk;
   end

   initial begin
      reset_n = 1'b1;
      #(10*`CLK_CYCLE)
      reset_n = 1'b0;
   end

   initial begin
      $monitor("pulse=%h \n", pulse_o);
      test_1();
      #(32*`CLK_CYCLE)
      test_2();
      #(6*`CLK_CYCLE)
      test_3();
   end

   task test_1();
      begin
         top <= 16'h0F;
      end
   endtask // test_1

   task test_2();
      begin      
         top = 16'h03;
         clr = 1'b1;
         #(1*`CLK_CYCLE)
         clr = 1'b0;
      end
   endtask // test_2

   task test_3();
      begin
         top = 16'hFF;
         clr = 1'b1;
         #(1*`CLK_CYCLE)
         clr = 1'b0;
      end
   endtask // test_3


   Modcntr dut(
               .clk(clk),
               .clr_in(clr),
               .top_in(top),
               .reset(reset_n),
               .pulse_out(pulse_o)
               );
   
endmodule
 
