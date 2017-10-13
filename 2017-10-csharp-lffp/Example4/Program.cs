using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.NetworkInformation;
using System.Security.Cryptography.X509Certificates;
using System.Text;
using System.Threading.Tasks;
using Readonly;

namespace Example4
{
    class Program
    {

        private static readonly Func<Turtle, string> GetName = turtle => turtle.Name;
        private static readonly Func<Turtle, Position> GetPosition = turtle => turtle.Position;
        private static readonly Func<Position, int> GetX = position => position.X;
        private static readonly Func<Position, int> GetY = position => position.Y;

        private static readonly Func<Turtle, string, Turtle> SetName = 
            (turtle, newName) => new Turtle(newName, GetPosition(turtle));

        private static readonly Func<Turtle, Position, Turtle> SetPosition = 
            (turtle, newPosition) => new Turtle(GetName(turtle), newPosition);
        
        private static readonly Func<Position, int, Position> SetX = (position, newX) => new Position(newX, GetY(position));
        private static readonly Func<Position, int, Position> SetY = (position, newY) => new Position(GetX(position), newY);

        private static readonly Reader<Turtle, string> Name = new Reader<Turtle, string>(GetName);
        private static readonly Reader<Turtle, Position> Position = new Reader<Turtle, Position>(GetPosition);
        private static readonly Reader<Position, int> X = new Reader<Position, int>(GetX);
        private static readonly Reader<Position, int> Y = new Reader<Position, int>(GetY);

        private static readonly Reader<Turtle, Tuple<string, int>> NameAndX = 
            new Reader<Turtle, Tuple<string, int>>(turtle =>
        {
            var name = Name.Run(turtle);
            var position = Position.Run(turtle);
            var x = X.Run(position);
            return Tuple.Create(name, x);
        });

        static void Main(string[] args)
        {

            var turtle = new Turtle("Bob", new Position(10, 12));

            var nameAndX = NameAndX.Run(turtle);

            var newX = 0;
            if (nameAndX.Item1 == "Bob" && nameAndX.Item2 < 7)
            {
                newX = 99;
            }
            else
            {
                newX = 13;
            }
            var turtle2 = SetPosition(turtle, SetX(GetPosition(turtle), newX));

            //Reader<Turtle, X> turtleX = Position.???(X)
            #region secret1
            var turtleX = Position.AndThen(X);
            #endregion

            //Reader<Turtle, Tuple<string, int>> nameAndX2 = Name.???(
            //  name => TurtleX.???(x => Tuple.Create(name, x)));            
            #region secret2
            Reader<Turtle, Tuple<string, int>> nameAndX2 = Name.
                SelectMany(name => turtleX.Select(
                    x => Tuple.Create(name, x)
                )
            );
            #endregion

            #region secret3
            var nameAndX3 =
                from name in Name
                from x in turtleX
                select Tuple.Create(name, x);
            #endregion
        }
    }
}
