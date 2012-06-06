#import "SettingsDao.h"
#import "SettingsService.h"
#import "Review.h"
#import "URLUtil.h"

static NSString *const PROMO_REVIEWS_KEY = @"promoReviews";
static NSString *const LOGO_KEY = @"logo";
static NSString *const COMPANY_NAME_KEY = @"companyName";
static NSString *const TABLET_ID_KEY = @"tabletId";
static NSString *const API_KEY_KEY = @"apiKey";
static NSString *const LAST_UPDATE_KEY = @"lastUpdate";
static NSString *const PHONE_PREFIX_KEY = @"phonePrefix"; 

static NSString *const DEFAULT_LOGO_IMAGE_NAME = @"welcome_company.png";
static NSString *const DEFAULT_SERVER_URL = @"http://likelabs.redwerk.com";
static NSString *const DEFAULT_COMPANY_NAME = @"[Vendor Name]";
static NSString *const DEFAULT_PHONE_PREFIX = @"+7";

@interface SettingsDao()

@property (nonatomic, assign) NSUserDefaults* settings;

@end

@implementation SettingsDao

@synthesize settings = _settings;

- (id)init {
    if (self = [super init]) {
        _settings = [NSUserDefaults standardUserDefaults];
    }
    return self;
}


- (void)setUserDefaults {
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    
    NSArray* defaultPromoReviews = [NSArray arrayWithObjects:
            [[[Review alloc] initWithUser:[[[User alloc] initWtithName:@"User1"] autorelease] andText:@"Sed ut perspiciatis, unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas sit, aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos, qui ratione voluptatem sequi nesciunt, neque porro quisquam est, qui dolorem ipsum, quia dolor sit, amet, consectetur"] autorelease],
            [[[Review alloc] initWithUser:[[[User alloc] initWtithName:@"User2"] autorelease] andText:@"adipisci velit, sed quia non numquam eius modi tempora incidunt, ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit, qui in ea voluptate velit esse, quam nihil molestiae consequatur, vel illum, qui dolorem eum fugiat, quo voluptas nulla pariatur? At vero eos et accusamus et iusto odio"] autorelease],
            [[[Review alloc] initWithUser:[[[User alloc] initWtithName:@"User3"] autorelease] andText:@"dignissimos ducimus, qui blanditiis praesentium voluptatum deleniti atque corrupti, quos dolores et quas molestias excepturi sint, obcaecati cupiditate non provident, similique sunt in culpa, qui officia deserunt mollitia animi, id est laborum et dolorum fuga."] autorelease],
            [[[Review alloc] initWithUser:[[[User alloc] initWtithName:@"User4"] autorelease] andText:@"Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio, cumque nihil impedit, quo minus id, quod maxime placeat, facere possimus, omnis voluptas assumenda est, omnis dolor repellendus."] autorelease],
            [[[Review alloc] initWithUser:[[[User alloc] initWtithName:@"User5"] autorelease] andText:@"Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet, ut et voluptates repudiandae sint et molestiae non recusandae. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat."] autorelease],
            nil];
    
    NSDictionary* settingsDict = [NSDictionary dictionaryWithObjectsAndKeys:DEFAULT_SERVER_URL, kServerUrlPreference, [NSKeyedArchiver archivedDataWithRootObject:defaultPromoReviews], PROMO_REVIEWS_KEY, UIImagePNGRepresentation([UIImage imageNamed:DEFAULT_LOGO_IMAGE_NAME]), LOGO_KEY, DEFAULT_COMPANY_NAME, COMPANY_NAME_KEY, [NSDate date], LAST_UPDATE_KEY, DEFAULT_PHONE_PREFIX, PHONE_PREFIX_KEY, nil];
    
    [defaults registerDefaults:settingsDict];
}

-(void)setPromoReviews:(NSArray *)reviews {
    NSData* data = [NSKeyedArchiver archivedDataWithRootObject:reviews];
    [self.settings setObject:data forKey:PROMO_REVIEWS_KEY];
    [self.settings synchronize];
}

-(NSArray*) promoReviews {
    return [NSKeyedUnarchiver unarchiveObjectWithData:[self.settings objectForKey:PROMO_REVIEWS_KEY]];
}

- (void)setLogo:(UIImage *)logo {
    [self.settings setObject:UIImagePNGRepresentation(logo) forKey:LOGO_KEY];
    [self.settings synchronize];
}

- (UIImage *)logo {
    return [UIImage imageWithData:[self.settings objectForKey:LOGO_KEY]];
}

- (void)setCompanyName:(NSString *)companyName {
    [self.settings setObject:companyName forKey:COMPANY_NAME_KEY];
    [self.settings synchronize];
}

- (NSString *)companyName {
    return [self.settings objectForKey:COMPANY_NAME_KEY];
}

- (void)setTabletId:(NSUInteger)tabletId {
    [self.settings setObject:[NSNumber numberWithUnsignedInteger:tabletId] forKey:TABLET_ID_KEY];
    [self.settings synchronize];
}

- (NSUInteger)tabletId {
    return ((NSNumber*)[self.settings objectForKey:TABLET_ID_KEY]).unsignedIntegerValue;
}

- (void)setApiKey:(NSString *)apiKey {
    [self.settings setObject:apiKey forKey:API_KEY_KEY];
    [self.settings synchronize];
}

- (NSString *)apiKey {
    return [self.settings objectForKey:API_KEY_KEY];
}

-(NSArray *)textReviews {
    NSArray* promoReviews = self.promoReviews;
    NSMutableArray* textReviews = [NSMutableArray arrayWithCapacity:promoReviews.count];
    for (Review* review in promoReviews) {
        if (review.reviewType == ReviewTypeText) {
            [textReviews addObject:review];
        }
    }
    return textReviews;    
}

- (void)setLastUpdate:(NSDate *)lastUpdate {
    [self.settings setObject:lastUpdate forKey:LAST_UPDATE_KEY];
    [self.settings synchronize];
}

- (NSDate *)lastUpdate {
    return [self.settings objectForKey:LAST_UPDATE_KEY];
}

- (NSString *)phonePrefix {
    return [self.settings objectForKey:PHONE_PREFIX_KEY];
}

@end
