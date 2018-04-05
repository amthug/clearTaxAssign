Program to read a single variable equation from json and solve.		

Json input:		
{
"op": "equal",
"lhs": {
        "op": "add",
        "lhs": 1,
        "rhs": {
                "op": "multiply",
                "lhs": "x",
                "rhs": 10
                }
         },
"rhs": 21
}

Output:
Equation: 1 + (x * 10) = 21				
x = (21-1)/10				
x = 2
