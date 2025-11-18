# Simple modulo counter set by top register

# Install prerequisites:

``` sh
    sudo apt update
    sudo apt install sbt
    sudo apt install gtkwave
```

# Build

Build by using:

``` sh
    sbt "runMain ModcntrSim"
```

# Simulate with gtkwave by using:

``` sh
   gtkwave simWorkspace/Modcntr/test.vcd
```

