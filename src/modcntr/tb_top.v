module tb_top;
   
   reg clk;
   reg reset_n;
   reg [15:0] top;
   reg clr;
   wire pulse_o;
       
   initial begin
      clk = 0;
      clr = 0;
      forever #5 clk = ~clk;
   end

   initial begin
      reset_n = 1'b1;
      #10
      reset_n = 1'b0;
   end

   initial begin
      test_1();
      $monitor("pulse=%h \n", pulse_o);
   end
   
   Modcntr dut(
               .clk(clk),
               .clr_in(clr),
               .top_in(top),
               .reset(reset_n),
               .pulse_out(pulse_o)
               );

   task test_1();
      top <= 16'h0F;
   endtask
   
endmodule
 
