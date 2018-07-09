rm(list = ls())

require(pso)

source("ReadMDSSData.R")
source("tasMdssOF.R")

Base = unique(ini_Location$ARRIVAL_BASE)

for (i in 1:length(Base)) {
  auxLofFuel = merge(lofFuel,
                     ini_Location[which(ini_Location$ARRIVAL_BASE == Base[i]),],
                     by.x = "Group.1",
                     by.y = "TAIL")
  
  if (nrow(auxLofFuel) > 1 &
      (max(auxLofFuel$perfFactor) != min(auxLofFuel$perfFactor == 1))) {
    lgthFlts = nrow(auxLofFuel)
    lgthTls = nrow(auxLofFuel)
    
    ## Using basic "optim" interface to minimize a function
    
    set.seed(1)
    
    #Fuel Cost
    fuelPrice = 421.5
    fuelCost = matrix(0,nrow = lgthTls, ncol = lgthFlts)
    
    for (i in 1:lgthTls) {
      for (j in 1:lgthFlts) {
        fuelCost[i,j] = auxLofFuel$perfFactor[i] *
          auxLofFuel$x[j] *
          fuelPrice;
      }
    }
    
    
    # Run PSO algorithm
    result = psoptim(
      rep(NA,lgthFlts),
      tasOf,
      lower = 1,
      upper = lgthTls,
      control = list(type = "SPSO2011",
                     #maxit = 5000,
                     #s=100,
                     trace = 0)
    )
    
    # print(round(result$par))
    alloc = round(result$par)
    print(auxLofFuel)
    for(k in 1:length(alloc)){
      print(paste(auxLofFuel$PATH[k], "->" ,auxLofFuel$Group.1[alloc[k]]))
      newPaths = which(Paths$PATH == auxLofFuel$PATH[k])
      Paths$NEW_TAIL[newPaths] = auxLofFuel$Group.1[alloc[k]]
      
    }
  }
  
}

save(Paths, file = "C:/Users/imedeiro/workspace/IVHM/src/resources/data/Paths.RData")