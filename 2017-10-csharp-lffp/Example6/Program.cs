using System;
using Example5;
using Readonly;

namespace Example6
{
    class Program
    {
        private static readonly Func<Turtle, string> GetName = turtle => turtle.Name;
        private static readonly Func<Turtle, Position> GetPosition = turtle => turtle.Position;

        private static readonly Func<Turtle, string, Turtle> SetName = (turtle, newName) => new Turtle(newName, GetPosition(turtle));
        private static readonly Func<Turtle, Position, Turtle> SetPosition = (turtle, newPosition) => new Turtle(GetName(turtle), newPosition);

        private static readonly Func<Position, int> GetX = position => position.X;
        private static readonly Func<Position, int> GetY = position => position.Y;
        private static readonly Func<Position, int, Position> SetX = (position, newX) => new Position(newX, GetY(position));
        private static readonly Func<Position, int, Position> SetY = (position, newY) => new Position(GetX(position), newY);

        private static readonly Lens<Turtle, string> Name = new Lens<Turtle, string>(GetName, SetName);
        private static readonly Lens<Turtle, Position> Position = new Lens<Turtle, Position>(GetPosition, SetPosition);
        
        private static readonly Lens<Position, int> X = new Lens<Position, int>(GetX, SetX);
        private static readonly Lens<Position, int> Y = new Lens<Position, int>(GetY, SetY);

        private static readonly Lens<Turtle, int> TurtleX = Position.AndThen(X);
        private static readonly Lens<Turtle, int> TurtleY = Position.AndThen(Y); 

        static void Main(string[] args)
        {
            var program =
                from name in Name.GetS()
                from x in TurtleX.GetS()
                from _1 in (name == "Bob" && x < 7)
                    ? TurtleX.SetS(99)
                    : TurtleX.SetS(17)
                select Unit.Only;

            var turtle1 = new Turtle("Bob", new Position(10, 12));
            var turtle2 = new Turtle("Larry", new Position(6, -2));
            var turtle3 = new Turtle("Bob", new Position(1, 1));

            Console.WriteLine(program.Run(turtle1).Item1);
            Console.WriteLine(program.Run(turtle2).Item1);
            Console.WriteLine(program.Run(turtle3).Item1);
        }
    }
}
