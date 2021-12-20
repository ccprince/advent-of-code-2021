Implementation notes
====================

I was afraid that I wouldn't be able to solve this one at all. A previous year included a similar problem, which I 
didn't solve, and it was only in two dimensions. Plus, after spending all day on yesterday's problem, I didn't want
to do the same today. I'd forgotten that he tends to make the weekend problems a little beefier than the weekday
ones; last weekend's problems weren't _that_ difficult.

That said, I've got the start of a solution. There were three big barriers:

1) How to overlap two regions, when each one is 2000 cells cubed: It's actually easy to determine the possible origins
   of the second region, by forcing one beacon in each group into the same spot with a simple vector subtraction.
   Given that, it's easy to try moving the second region to each of the possible origins, and checking for intersections.
2) How to associate the overlapping pairs: I don't think I actually have to worry about that. If I start with region 0,
   I can find any region that overlaps with it -- there must be at least one -- then combine them. I should then be
   able to keep finding regions that overlap with that combined region.

   If that doesn't work, I might have to overlap all the pairs, then traverse the graph of those pairs to put them
   all together. I hope it doesn't come to that.
3) How to take a region and transform it into all 24 possible orientations: I don't have my head wrapped around that,
   yet. I'm sure it's something simple, and I'm just missing it.

At any rate, I'm going to put this aside for a while. I may not finish it before Christmas, but it would be nice to 
get a solution.