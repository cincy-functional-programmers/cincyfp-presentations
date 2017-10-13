using Example4;
using Readonly;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Example5
{
    public sealed class Unit
    {
        public static readonly Unit Only = new Unit();
        private Unit() { }
    }

    class Program
    {
        public static Func<Turtle, string> GetName = turtle => turtle.Name;
        public static Func<Turtle, Position> GetPosition = turtle => turtle.Position;

        public static Func<Turtle, string, Turtle> SetName = (turtle, newName) => new Turtle(newName, GetPosition(turtle));
        public static Func<Turtle, Position, Turtle> SetPosition = (turtle, newPosition) => new Turtle(GetName(turtle), newPosition);

        public static Func<Position, int> GetX = position => position.X;
        public static Func<Position, int> GetY = position => position.Y;
        public static Func<Position, int, Position> SetX = (position, newX) => new Position(newX, GetY(position));
        public static Func<Position, int, Position> SetY = (position, newY) => new Position(GetX(position), newY);

        static void Main(string[] args)
        {
            var program =
                from name in GetName.Get()
                from pos in GetPosition.Get()                 
                let x = GetX(pos)
                from _2 in (name == "Bob" && x < 7)
                    ? StateExtensions.Mod<Turtle>(t => SetPosition(t, SetX(pos, 99)))
                    : StateExtensions.Mod<Turtle>(t => SetPosition(t, SetX(pos, 17)))
                select Unit.Only;

            var turtle = new Turtle("Bob", new Position(10, 12));
            Console.WriteLine(turtle);
            var turtle2 = program.Run(turtle).Item1;
            Console.WriteLine(turtle2);
        }
    }
}
