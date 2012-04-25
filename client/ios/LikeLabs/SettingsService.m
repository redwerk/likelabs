#import "SettingsService.h"
#import "Review.h"
#import "User.h"

@implementation SettingsService

-(NSArray *)getTextReviews {
    /*User* user = [[User alloc] init];
    user.name = @"user";
    user.phoneNumber = @"12345";
    user.password = @"asdf";
    Review* review = [[[Review alloc] initWithUser: user andText:@"text"] autorelease];*/
    
    return [NSArray arrayWithObjects:
            [[[Review alloc] initWithUser:[[[User alloc] initWtithName:@"User1" phone:@"1234" andPassword:@"431"] autorelease] andText:@"Sed ut perspiciatis, unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas sit, aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos, qui ratione voluptatem sequi nesciunt, neque porro quisquam est, qui dolorem ipsum, quia dolor sit, amet, consectetur"] autorelease],
            [[[Review alloc] initWithUser:[[[User alloc] initWtithName:@"User2" phone:@"1234" andPassword:@"431"] autorelease] andText:@"adipisci velit, sed quia non numquam eius modi tempora incidunt, ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit, qui in ea voluptate velit esse, quam nihil molestiae consequatur, vel illum, qui dolorem eum fugiat, quo voluptas nulla pariatur? At vero eos et accusamus et iusto odio"] autorelease],
            [[[Review alloc] initWithUser:[[[User alloc] initWtithName:@"User3" phone:@"1234" andPassword:@"431"] autorelease] andText:@"dignissimos ducimus, qui blanditiis praesentium voluptatum deleniti atque corrupti, quos dolores et quas molestias excepturi sint, obcaecati cupiditate non provident, similique sunt in culpa, qui officia deserunt mollitia animi, id est laborum et dolorum fuga."] autorelease],
            [[[Review alloc] initWithUser:[[[User alloc] initWtithName:@"User4" phone:@"1234" andPassword:@"431"] autorelease] andText:@"Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio, cumque nihil impedit, quo minus id, quod maxime placeat, facere possimus, omnis voluptas assumenda est, omnis dolor repellendus."] autorelease],
            [[[Review alloc] initWithUser:[[[User alloc] initWtithName:@"User5" phone:@"1234" andPassword:@"431"] autorelease] andText:@"Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet, ut et voluptates repudiandae sint et molestiae non recusandae. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat."] autorelease],
            nil];
}

@end
