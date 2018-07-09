#fitness function
tasOf <- function(x) {
  tasCost = 0;
  
  if (max(table(round(x))) > 1) {
    tasCost = 1e20
    
  }else{
    for (i in 1:length(x)) {
      tasCost = tasCost + fuelCost[x[i],i];
    }
  }
  
  
  return(tasCost)
}
