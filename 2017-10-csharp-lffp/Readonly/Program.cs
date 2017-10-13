using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Readonly
{
    class Program
    {
        static void Main(string[] args)
        {
            var turtle = new Turtle("Bob", new Position(10,12));

            Console.WriteLine(turtle);

            var turtle2 = new Turtle(turtle.Name, new Position(turtle.Position.X - 9, turtle.Position.Y));

            Console.WriteLine(turtle2);
            Console.WriteLine(turtle);

            Turtle turtle3;
            if (turtle2.Name == "Bob" && turtle2.Position.X < 7)
            {
                turtle3 = new Turtle(turtle2.Name, new Position(99, turtle2.Position.Y));
            }
            else
            {
                turtle3 = new Turtle(turtle2.Name, new Position(17, turtle2.Position.Y));
            }

            Console.WriteLine(turtle3);
        }
    }
}
