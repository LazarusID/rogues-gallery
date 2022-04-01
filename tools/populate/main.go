package main

import (
	"database/sql"
	_ "embed"
	"flag"
	"fmt"
	"log"
	"math/rand"
	"strings"

	_ "github.com/jackc/pgx/v4"
)

//go:embed resources/prenom.txt
var rawprenom string

//go:embed resources/surnom.txt
var rawsurnom string

var db *sql.DB

var userGoal int
var campaignsPerUser int
var galleriesPerCampaign int

var userStmt *sql.Stmt
var campaignStmt *sql.Stmt
var galleryStmt *sql.Stmt

var prenom []string
var surnom []string

func addUser() {
	name := makeName()
	email := strings.Replace(name, " ", "_", 2) + "@mailinator.org"
	res, err := userStmt.Exec(name, email)
	if err != nil {
		log.Fatalf("user insert: %v", err)
	}
	userid, err := res.LastInsertId()
	if err != nil {
		log.Fatalf("retrieving user id: %v", err)
	}

	addCampaign(userid)

	log.Printf("created %s", name)
}

func addCampaign(userId int64) {
	goal := rand.Intn(campaignsPerUser) + 1
	for i := 0; i < goal; i++ {
		res, err := campaignStmt.Exec(userId, makeCampaignName())
		if err != nil {
			log.Fatalf("campaign insert: %v", err)
		}
		campaignId, err := res.LastInsertId()
		if err != nil {
			log.Fatalf("retrieving campaign id: %v", err)
		}
		addGallery(campaignId)
	}
}

func addGallery(campaignId int64) {
	goal := rand.Intn(galleriesPerCampaign) + 1
	for i := 0; i < goal; i++ {
		name := makeName()
		galleryname := fmt.Sprintf("%s.adv", strings.Replace(name, " ", "-", -1))
		content := fmt.Sprintf("rival %s: Adversary 1", name)
		_, err := galleryStmt.Exec(campaignId, galleryname, content)
		if err != nil {
			log.Fatalf("gallery insert: %v", err)
		}
	}
}

func main() {

	flag.IntVar(&userGoal, "users", 100, "Number of users to add")
	flag.IntVar(&campaignsPerUser, "campaigns", 4, "Max number of campaigns per user")
	flag.IntVar(&galleriesPerCampaign, "galleries", 6, "Max number of galleries per campaign")

	flag.Parse()

	fmt.Printf("Creating %d users, 1-%d campaigns per user, 1-%d galleries per campaign\n",
		userGoal, campaignsPerUser, galleriesPerCampaign)

	db, err := sql.Open("pgx", "postgres://blakeney:scarletpimpernil@localhost/blakeney?sslmode=disable")
	if err != nil {
		log.Fatal(err)
	}
	defer db.Close()

	userStmt, err = db.Prepare("INSERT INTO appuser (name, email) VALUES ($1, $2);")
	if err != nil {
		log.Fatalf("user statement: %v", err)
	}
	defer userStmt.Close()

	campaignStmt, err = db.Prepare("INSERT INTO campaign (user_id, name, active) VALUES ($1, $2, true);")
	if err != nil {
		log.Fatalf("campaign statement: %v", err)
	}
	defer campaignStmt.Close()

	galleryStmt, err = db.Prepare("INSERT INTO gallery (campaign_id, name, content, active) VALUES ($1, $2, $3, true);")
	if err != nil {
		log.Fatalf("gallery statement: %v", err)
	}
	defer galleryStmt.Close()

	loadNames()

	for i := 0; i < userGoal; i++ {
		addUser()
	}

}

func loadNames() {
	prenom = strings.Split(rawprenom, "\n")
	surnom = strings.Split(rawsurnom, "\n")
}

func makeName() string {
	prenomlen := len(prenom)
	surnomlen := len(surnom)

	first := prenom[rand.Intn(prenomlen)]
	second := surnom[rand.Intn(surnomlen)]

	return fmt.Sprintf("%s %s", first, second)
}

func makeCampaignName() string {
	var ADJECTIVES = []string{"autumn", "hidden", "bitter", "misty", "silent", "empty", "dry", "dark", "summer",
		"icy", "delicate", "quiet", "white", "cool", "spring", "winter", "patient",
		"twilight", "dawn", "crimson", "wispy", "weathered", "blue", "billowing",
		"broken", "cold", "damp", "falling", "frosty", "green", "long", "late", "lingering",
		"bold", "little", "morning", "muddy", "old", "red", "rough", "still", "small",
		"sparkling", "throbbing", "shy", "wandering", "withered", "wild", "black",
		"young", "holy", "solitary", "fragrant", "aged", "snowy", "proud", "floral",
		"restless", "divine", "polished", "ancient", "purple", "lively", "nameless"}

	var NOUNS = []string{"waterfall", "river", "breeze", "moon", "rain", "wind", "sea", "morning",
		"snow", "lake", "sunset", "pine", "shadow", "leaf", "dawn", "glitter", "forest",
		"hill", "cloud", "meadow", "sun", "glade", "bird", "brook", "butterfly",
		"bush", "dew", "dust", "field", "fire", "flower", "firefly", "feather", "grass",
		"haze", "mountain", "night", "pond", "darkness", "snowflake", "silence",
		"sound", "sky", "shape", "surf", "thunder", "violet", "water", "wildflower",
		"wave", "water", "resonance", "sun", "wood", "dream", "cherry", "tree", "fog",
		"frost", "voice", "paper", "frog", "smoke", "star"}

	adjLen := len(ADJECTIVES)
	nounLen := len(NOUNS)

	return fmt.Sprintf("%s-%s", ADJECTIVES[rand.Intn(adjLen)], NOUNS[rand.Intn(nounLen)])
}
