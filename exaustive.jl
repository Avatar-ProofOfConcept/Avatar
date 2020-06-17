#!/home/amal/Téléchargements/julia-1.4.2/bin/julia
using vOptGeneric
using LinearAlgebra
using DelimitedFiles
using GLPK,GLPKMathProgInterface
m = vModel(solver = GLPKSolverMIP())
# using CPLEX
# m = vModel(solver = CplexSolver())

time =readdlm(IOBuffer(ARGS[1]),' ',';')

throughput=readdlm(IOBuffer(ARGS[2]),' ',';')

#Utilités p
u =readdlm(IOBuffer(ARGS[3]),' ',';') 
  

#Fluctuation f
f =readdlm(IOBuffer(ARGS[4]),' ',';')

#Contraintes
et=1000
ed=0.001
 

#Formulation
@variable(m, x[1:size(f, 1),1:size(f, 2)], Bin)
@addobjective(m, Min, sum((-1)*x[i,j]*u[i,j] for i=1:size(f, 1),j=1:size(f, 2)))
@addobjective(m, Min,sum(x[i,j]*f[i,j] for i=1:size(f, 1),j=1:size(f, 2)))

@constraint(m, rows1[j=1:size(f, 2)],sum(x[i,j] for i=1:size(f, 1))==1)

@constraint(m,sum(x[i,j]*time[i,j] for i=1:size(f, 1),j=1:size(f, 2)) <= et)
@constraint(m,sum(x[i,j]*log(throughput[i,j]) for i=1:size(f, 1),j=1:size(f, 2)) >= log(ed))

 
solve(m, method=:dichotomy)

Y_N = getY_N(m)

for n = 1:length(Y_N)
    X = getvalue(x, n)
    println(X)
    #println(Y_N[n])
end
