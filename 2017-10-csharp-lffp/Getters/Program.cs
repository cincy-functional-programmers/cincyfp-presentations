using System;
using Readonly;

namespace Getters
{
    class Program
    {
        private static readonly Func<Turtle, string> GetName = turtle => turtle.Name;
        private static readonly Func<Turtle, Position> GetPosition = turtle => turtle.Position;

        private static Func<Position, int> GetX = position => position.X;
        private static Func<Position, int> GetY = position => position.Y;

        private static Func<Turtle, string, Turtle> SetName = 
            (turtle, newName) => new Turtle(newName, GetPosition(turtle)); 

        private static Func<Turtle, Position, Turtle> SetPosition = 
            (turtle, newPosition) => new Turtle(GetName(turtle), newPosition);

        private static Func<Position, int, Position> SetX = (position, newX) => new Position(newX, GetY(position));
        private static Func<Position, int, Position> SetY = (position, newY) => new Position(GetX(position), newY);  

        static void Main(string[] args)
        {
            var turtle1 = new Turtle("Bob", new Position(10, 12));

            Console.WriteLine(turtle1);

            var turtle2 = SetPosition(turtle1, SetX(GetPosition(turtle1), GetX(GetPosition(turtle1)) - 9));

            Console.WriteLine(turtle2);
            Console.WriteLine(turtle1);

            var name = GetName(turtle2);
            var position = GetPosition(turtle2);
            var x = GetX(position);
            var newX = 0;
            if (name == "Bob" && x < 7)
            {
                newX = 99;
            }
            else
            {
                newX = 17;
            }
            var newPosition = SetX(position, newX);
            var turtle3 = SetPosition(turtle2, newPosition);

            Console.WriteLine(turtle3);
        }
    }
}
