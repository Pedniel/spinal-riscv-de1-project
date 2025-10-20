library ieee;
use ieee.std_logic_1164.all;

entity t_de1_murax_franz is
end t_de1_murax_franz;

architecture tbench of t_de1_murax_franz is

  -- definition of a clock period
  constant period : time    := 20 ns;
  -- switch for clock generator
  signal clken_p  : boolean := true;


  signal clk_i    : std_ulogic;
  signal rst_ni   : std_ulogic;
  signal key      : std_ulogic;
  signal ledr : std_ulogic_vector(7 downto 0);
  
  signal tck, tdi, tms, tdo : std_logic;
  signal uart_rxd, uart_txd : std_logic;

begin

  -- clock generation
  clock_proc : process
  begin
    while clken_p loop
      clk_i <= '0'; wait for period/2;
      clk_i <= '1'; wait for period/2;
    end loop;
    wait;
  end process;
  
  -- initial reset, always necessary at the beginning of a simulation
  reset : rst_ni <= '0', '1' AFTER period;

  stimuli_p : process
    begin
      wait until rst_ni = '1';      
      wait for 15*period;
      --wait until falling_edge(clk_i) and ledr = "00010"; 

      wait;
      clken_p <= false;
      wait;    
  end process stimuli_p;
  
  tck <= '0';
  tdi <= '0';
  tms <= '0';
  uart_rxd <= '1';


  de1_murax_franz_i0 : entity work.de1_murax_franz
    port map (
      CLOCK_50  => clk_i,
      JTAG_TCK  => tck,
      JTAG_TDI  => tdi,
      JTAG_TDO  => tdo,
      JTAG_TMS  => tms,
      UART_RXD  => uart_rxd,
      UART_TXD  => uart_txd,
      KEY0      => rst_ni,
      LEDR      => ledr);
      
  simstop_p : process
  begin
    wait;
    wait on clken_p for 3000 ms;     
    assert not clken_p report "Simulation failed due to timeout" severity failure;
    wait; 
  end process; 

end tbench;
