#import "SettingsService.h"
#import "Review.h"
#import "User.h"
#import "URLUtil.h"
#import "SettingsParser.h"
#import "SettingsDao.h"

@interface SettingsService()

@property (nonatomic, retain) NSArray* promoReviews;
@property (nonatomic, retain) SettingsDao* dao;

- (void) getPhotosForReviews:(NSArray*) reviews;
- (void)getLogoByUrl:(NSString*) logoUrl;

@end


@implementation SettingsService

@synthesize dao = _dao;
@synthesize promoReviews = _promoReviews;

static NSString *const REVIEW_KEY = @"review";
static NSUInteger const SETTINGS_RESPONSE_STATUS_OK = 200;

- (id)init {
    if (self = [super init]) {
        _dao = [[SettingsDao alloc] init];
    }
    return self;
}

- (void)getSettings {    
    ASIHTTPRequest *request = [[ASIHTTPRequest alloc] initWithURL:[URLUtil getSettingsUrlForTablet:self.dao.tabletId]];
    [request addRequestHeader:TABLET_API_KEY value:self.dao.apiKey];
    [request setDelegate:self];
    [request startAsynchronous];
}

#pragma mark - Get settings (initial)

- (void)requestFinished:(ASIHTTPRequest *)request {
    NSLog(@"Settings response status: %d", request.responseStatusCode);
    NSLog(@"Settings response string: %@", request.responseString);
    if (request.responseStatusCode == SETTINGS_RESPONSE_STATUS_OK) {
        self.dao.lastUpdate = [NSDate date];
    }
    
    NSXMLParser *xmlParser = [[NSXMLParser alloc] initWithData:request.responseData];
    SettingsParser* settingsParser = [[SettingsParser alloc] init];
    [xmlParser setDelegate:settingsParser];
    [xmlParser parse];
    [xmlParser release];
    
    self.promoReviews = settingsParser.promoReviews;    
    [self getPhotosForReviews:settingsParser.promoReviews];
    [self getLogoByUrl:settingsParser.companyLogoUrl];
    self.dao.companyName = settingsParser.companyName;
    
    [settingsParser release];
}

- (void)requestFailed:(ASIHTTPRequest *)request {
    NSLog(@"Error getting settings: %@", request.error);
}

#pragma mark - Get photos by URLs

- (void) getPhotosForReviews:(NSArray*) reviews {
    for (Review* review in reviews) {       
        if (review.reviewType == ReviewTypePhoto) {
            ASIHTTPRequest *photoRequest = [[ASIHTTPRequest alloc] initWithURL:[NSURL URLWithString:review.imageUrl]];
            [photoRequest setDelegate:self];
            photoRequest.userInfo = [NSDictionary dictionaryWithObject:review forKey:REVIEW_KEY];
            [photoRequest addRequestHeader:TABLET_API_KEY value:self.dao.apiKey];
            [photoRequest setDidFinishSelector:@selector(photoReceived:)];
            [photoRequest setDidFailSelector:@selector(photoRequestFailed:)];
            [photoRequest startAsynchronous];
        }
    }
}

- (void)photoReceived:(ASIHTTPRequest *)request {
    UIImage* photoImage = [UIImage imageWithData:request.responseData];
    
    Photo* photo = [[Photo alloc] initWithImage:photoImage];
    Review* review = [request.userInfo objectForKey:REVIEW_KEY];
    review.photos = [NSArray arrayWithObject:photo];
    [photo release];
    
    self.dao.promoReviews = self.promoReviews;
}

- (void)photoRequestFailed:(ASIHTTPRequest *)request {
    NSLog(@"Error getting photo: %@", request.error);
}

#pragma mark - Get logo by URL

- (void)getLogoByUrl:(NSString*) logoUrl {
    ASIHTTPRequest *logoRequest = [[ASIHTTPRequest alloc] initWithURL:[NSURL URLWithString:logoUrl]];
    [logoRequest setDelegate:self];
    [logoRequest addRequestHeader:TABLET_API_KEY value:self.dao.apiKey];
    [logoRequest setDidFinishSelector:@selector(logoReceived:)];
    [logoRequest setDidFailSelector:@selector(logoRequestFailed:)];
    [logoRequest startAsynchronous];
}

- (void)logoReceived:(ASIHTTPRequest *)request {
    UIImage* logoImage = [UIImage imageWithData:request.responseData];
    self.dao.logo = logoImage;
}

- (void)logoRequestFailed:(ASIHTTPRequest *)request {
    NSLog(@"Error getting logo: %@", request.error);
}

#pragma mark - Memory

- (void)dealloc {
    self.promoReviews = nil;
    self.dao = nil;
    [_dao release];
    [_promoReviews release];
    [super dealloc];
}

@end
