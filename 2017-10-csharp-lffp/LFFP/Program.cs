using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LFFP
{
    class Program
    {
        static void Main(string[] args)
        {
            var turtle = new Turtle
            {
                Name = "Bob",
                Position = new Position
                {
                    X = 10,
                    Y = 12
                }
            };
            
            Console.WriteLine(turtle);

            turtle.Position.X -= 9;
            
            Console.WriteLine(turtle);
        }
    }
}
