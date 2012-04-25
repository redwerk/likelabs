#import <Foundation/Foundation.h>

@class Tablet;

@interface SettingsDao : NSObject


NSString* getCompanyName();
void setCompanyTame(NSString* name);

- (id) getLogo;
- (void) setLogo:(id) logo;

- (Tablet*) getTablet;
- (void) setTablet:(Tablet*) tablet;

- (NSArray*) getWelcomeReviews;
- (void) setWelcomeReviews:(NSArray*) reviews;

- (NSArray*) getTextReviews;
- (void) setTextReviews:(NSArray*) comments;

@end
